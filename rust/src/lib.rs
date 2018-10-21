pub mod cnp {
    use std::fmt;

    pub fn valid_cnp(number: &str) -> bool {
        cnp_from_string(number).is_some()
    }

    pub fn valid_nif(number: &str) -> bool {
        false
    }

    pub fn valid_number(number: &str) -> bool {
        if looks_like_nif(number) {
            valid_nif(number)
        } else {
            valid_cnp(number)
        }
    }

    fn looks_like_nif(number: &str) -> bool {
        number.chars().next() == Some('9')
    }

    #[derive(Debug, PartialEq, Eq)]
    pub enum Sex {
        Male,
        Female
    }

    #[derive(Debug, PartialEq, Eq)]
    pub struct Cnp {
        pub sex: Sex,
        pub birth_year: u16,
        pub birth_month: u8,
        pub birth_day: u8,
        pub county: u8,
        pub ordinal: u16,
        pub born_abroad: bool
    }

    impl Cnp {
        fn sex_char(&self) -> char {
            if self.born_abroad {
                match self.sex {
                    Sex::Male => '7',
                    Sex::Female => '8'
                }
            } else {
                match self.birth_year / 100 {
                    18 => match self.sex {
                        Sex::Male => '3',
                        Sex::Female => '4'
                    },
                    19 => match self.sex {
                        Sex::Male => '1',
                        Sex::Female => '2'
                    },
                    20 => match self.sex {
                        Sex::Male => '5',
                        Sex::Female => '6'
                    }
                    _ => panic!("Invalid birth year")
                }
            }
        }

        fn chars12(&self) -> Vec<char> {
            let digits: String = format!("{}{:02}{:02}{:02}{:02}{:03}",
                self.sex_char(),
                self.birth_year % 100,
                self.birth_month,
                self.birth_day,
                self.county,
                self.ordinal);
            digits.chars().collect()
        }

        pub fn stringify(&self) -> String {
            let first_chars = self.chars12();
            let check_digit = check_digit(&first_chars);
            [first_chars.into_iter().collect(), check_digit.to_string()].concat()
        }
    }

    impl fmt::Display for Cnp {
        fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
            write!(f, "CNP({})", self.stringify())
        }
    }

    pub fn cnp_from_string(number: &str) -> Option<Cnp> {
        let digits: Vec<char> = number.chars().collect();
        if digits.len() != 13 {
            return None;
        }
        if ! digits.iter().all(|&c| c.is_ascii_digit()) {
            return None;
        }
        if check_digit(&digits[0..12]) != digits[12] {
            return None;
        }
        let sex = match digits[0] {
            '1' | '3' | '5' | '7' => Sex::Male,
            '2' | '4' | '6' | '8' => Sex::Female,
            _ => return None
        };
        let century_base: u16 = match digits[0] {
            '1' | '2' | '7' | '8' => 1900,
            '3' | '4' => 1800,
            '5' | '6' => 2000,
            _ => return None
        };
        let two_digit_year = number[1..3].parse::<u16>().unwrap();
        let birth_year: u16 = century_base + two_digit_year;
        let birth_month: u8 = number[3..5].parse::<u8>().unwrap();
        let birth_day: u8 = number[5..7].parse::<u8>().unwrap();
        let county: u8 = number[7..9].parse::<u8>().unwrap();
        let ordinal: u16 = number[9..12].parse::<u16>().unwrap();
        let born_abroad: bool = match digits[0] {
            '7' | '8' => true,
            _ => false
        };
        Some(Cnp {sex: sex, birth_year: birth_year, birth_month: birth_month, birth_day: birth_day, county: county, ordinal: ordinal, born_abroad: born_abroad })
    }

    fn check_digit(digits: &[char]) -> char {
        static FACTORS: [u32; 12] = [2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9];
        let digit_tuples = digits.iter().
            map(|&c| c.to_digit(10).unwrap()).
            zip(FACTORS.iter());
        let sum: u32 = digit_tuples.
            map(|(a, b)| a * b).
            fold(0, |x, y| x + y );
        let remainder: u32 = if sum % 11 == 10 { 1 } else { sum % 11 };
        use std::char;
        char::from_digit(remainder, 10).unwrap()
    }

    #[cfg(test)]
    mod tests {
        use cnp;

        #[test]
        fn it_validates_number() {
            assert!(cnp::valid_number("1690509049993"))
        }

        #[test]
        fn it_validates_cnp() {
            assert!(cnp::valid_cnp("1690509049993"))
        }

        #[test]
        fn it_parses_well_known_cnp() {
            let cnp = cnp::cnp_from_string("1690509049993").unwrap();
            assert_eq!(cnp::Sex::Male, cnp.sex);
            assert_eq!(1969, cnp.birth_year);
            assert_eq!(5, cnp.birth_month);
            assert_eq!(9, cnp.birth_day);
            assert_eq!(4, cnp.county);
            assert_eq!(999, cnp.ordinal);
            assert_eq!(false, cnp.born_abroad);
        }

        #[test]
        fn it_rejects_cnp_with_wrong_check_digit() {
            assert_eq!(None, cnp::cnp_from_string("1690509049990"))
        }
//
//        #[test]
//        fn it_rejects_cnp_with_invalid_date() {
//            assert_eq!(None, cnp::cnp_from_string("1691301049995"))
//        }

        #[test]
        fn it_rejects_ill_formed_cnp() {
            assert_eq!(None, cnp::cnp_from_string("1j91301049995"))
        }

        #[test]
        fn it_accepts_cnp_with_nonstandard_county() {
            let cnp = cnp::cnp_from_string("1690509990011").unwrap();
            assert_eq!(cnp::Sex::Male, cnp.sex);
            assert_eq!(1969, cnp.birth_year);
            assert_eq!(5, cnp.birth_month);
            assert_eq!(9, cnp.birth_day);
            assert_eq!(99, cnp.county);
            assert_eq!(1, cnp.ordinal);
            assert_eq!(false, cnp.born_abroad);
        }

        #[test]
        fn it_creates_cnp_from_components() {
            let cnp = cnp::Cnp {
                sex: cnp::Sex::Male,
                birth_year: 1969,
                birth_month: 5,
                birth_day: 9,
                county: 4,
                ordinal: 999,
                born_abroad: false };
            assert_eq!("1690509049993", cnp.stringify());
        }
    }
}

