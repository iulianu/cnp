# Usage

```rust
use cnp;

cnp::valid_cnp("1690509049993")
// => true

match cnp::cnp_from_string("1690509049993") {
    Some(cnp) => println!("{} born in year {}", cnp, cnp.birth_year),
    None => println!("Invalid CNP")
}

```

# WIP

This is work in progress. Validations are missing; support for NIFs is missing.