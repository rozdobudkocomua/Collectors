package Collectors.Interfaces;

import Collectors.Objects.Person;

public interface DebtBook {

    void add (Person person);

    void update (Person person);

    void delete (Person person);
}
