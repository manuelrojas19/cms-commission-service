package com.manuelr.microservices.cms.commissionservice.config;

import com.manuelr.microservices.cms.commissionservice.entity.Employee;
import com.manuelr.microservices.cms.commissionservice.entity.Manager;
import com.manuelr.microservices.cms.commissionservice.entity.Person;
import com.manuelr.microservices.cms.commissionservice.repository.EmployeeRepository;
import com.manuelr.microservices.cms.commissionservice.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Configuration
public class SignupPersonHandler {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void savePerson(Person person, Consumer<Person> personConsumer) {
        personConsumer.andThen(this::savePerson).accept(person);
    }

    @Transactional
    public void savePerson(Person person, Long managerId, Consumer<Person> personConsumer) {
        Manager manager = managerRepository.findByPersonId(managerId)
                .orElseThrow(() -> new RuntimeException());
        Employee employee = (Employee) person;
        employee.setManager(manager);
        personConsumer.andThen(this::savePerson).accept(employee);
    }

    private void savePerson(Person person) {
        if (person instanceof Manager)
            managerRepository.save((Manager) person);
        else if (person instanceof Employee)
            employeeRepository.save((Employee) person);
    }
}
