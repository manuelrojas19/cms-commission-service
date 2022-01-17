package com.manuelr.microservices.cms.commissionservice.config;

import com.manuelr.cms.commons.dto.EmployeeDto;
import com.manuelr.cms.commons.dto.ManagerDto;
import com.manuelr.cms.commons.dto.PersonDto;
import com.manuelr.cms.commons.event.registration.RegistrationEvent;
import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import com.manuelr.microservices.cms.commissionservice.entity.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class EventHandlerConfig {

    @Autowired
    private SignupPersonHandler handler;

    @Bean
    public Consumer<RegistrationEvent> registrationEventConsumer() {
        return re -> {
            PersonDto personDto = re.getPersonDto();
            if (personDto instanceof ManagerDto) {
                handler.savePerson(new Manager(), person -> person.setPersonId(personDto.getId()));
            } else if (personDto instanceof EmployeeDto) {
                handler.savePerson(new Employee(), ((EmployeeDto) personDto).getManagerId(),
                        person -> person.setPersonId(personDto.getId()));
            }
        };
    }
}
