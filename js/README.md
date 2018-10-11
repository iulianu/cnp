# Usage

Any modern browser or recent Node.js version is supported.

```javascript
// Validation of CNPs and NIFs
const CNP = require('./cnp.js');

CNP.isValid("1690509049993") // CNP
// => true
CNP.isValid("9000123456785") // NIF
// => true


// Destructuring of CNP
let cnp = CNP.cnpFromString("1690509049993")
cnp.sex
// => "m"
cnp.birthYear
// => 1969
cnp.county
// => 4
cnp.bornAbroad
// => false
```


