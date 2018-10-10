# Usage

```ruby
# Validation of CNPs and NIFs
require_relative 'lib/cnp'

Cnp::RomanianPersonalNumber.valid?("1690509049993") # CNP
# => true
Cnp::RomanianPersonalNumber.valid?("9000123456785") # NIF
# => true


# Destructuring of CNP
cnp = Cnp::RomanianPersonalNumber.from_string("1690509049993")
cnp.kind_of?(Cnp::Cnp)
# => true
cnp.sex
# => :m
cnp.date.to_s
# => "1969-05-09"
cnp.county
# => 4
cnp.born_abroad
# => false
```
