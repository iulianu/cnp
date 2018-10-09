# Usage

```java
// Validation of CNPs and NIFs
import rocks.iulian.cnp.*;
// [...]
RomanianPersonalNumber.isValid("1690509049993") // CNP
// => true
RomanianPersonalNumber.isValid("9000123456785") // NIF
// => true


// Destructuring of CNP
RomanianPersonalNumber.fromString("1690509049993") instanceof Cnp
// => true

Cnp cnp = (Cnp) RomanianPersonalNumber.fromString("1690509049993");
cnp.getSex()
// => MALE
cnp.getBirthYear()
// => 1969
cnp.getCounty()
// => 4
cnp.isBornAbroad()
// => false
```

# Installation

This library requires Java 1.7 and Maven 3.

`mvn clean install`

