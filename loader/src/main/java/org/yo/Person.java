package org.yo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Person {
    private int age;
    private String name;
    private String gender;
    private Address address;
}
