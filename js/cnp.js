/**
 * The CNP has the following structure:
 * SAALLZZJJNNNC
 * S = sex digit, also encodes century of birth
 * AA = year of birth, last two digits
 * LL = month of birth, 01-12
 * ZZ = day of birth, 01-31
 * JJ = county where birth was registered,
 *      or for people born before 1978, county where
 *      the person resided when CNP was issued.
 *      Typical values for this field are 01-39, 41-46, and,
 *      starting with 1981, also 51 and 52.
 *      but CNPs with other values exist as well.
 * NNN = ordinal
 *  TODO Unclear if the ordinal (NNN) can be 000 or starts at 001
 * C = check digit.
 *
 * A NIF is kind of a CNP but without inner structure
 * and with the leading digit '9'.
 * The CNP check digit rules apply.
 *
 */

const isValidDate = function(year, mon, day) {
  let date = new Date();
  date.setFullYear(year, mon - 1, day);
  return date.getFullYear() === year &&
    date.getMonth() === mon - 1 &&
    date.getDate() === day;
};

const pad = function(num, size) {
  let s = num + "";
  while (s.length < size) s = "0" + s;
  return s;
};

const checkDigit = function(str12) {
  const CHECK_DIGIT_FACTORS = [2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9];
  let sum = 0;
  for( let i = 0; i < str12.length; i++ ) {
    sum += parseInt(str12.charAt(i), 10) * CHECK_DIGIT_FACTORS[i];
  }
  sum %= 11;
  if(sum === 10) {
    return '1';
  } else {
    return sum.toString();
  }
};

// 'this' must be bound to an object that supports .stringify12()
const stringify = function() {
  let s12 = this.stringify12();
  return s12 + checkDigit(s12);
};

const CNP = function(sex, birthYear, birthMonth, birthDay, county, ordinal, bornAbroad) {
  // Coercion
  birthYear = +birthYear;
  birthMonth = +birthMonth;
  birthDay = +birthDay;
  county = +county;
  ordinal = +ordinal;
  bornAbroad = !!bornAbroad;

  if(sex !== "m" && sex !== "f") {
    throw "Invalid sex";
  }
  if( !isValidDate(birthYear, birthMonth, birthDay) ) {
    throw "Invalid birth date";
  }
  if( birthYear < 1800 || birthYear > 2099 ) {
    throw "Birth date out of range";
  }
  if( bornAbroad && (birthYear < 1900 || birthYear > 1999) ) {
    throw "Birth date out of range for people born abroad";
  }
  if( ordinal < 0 || ordinal > 999 ) {
    throw "Ordinal out of range";
  }

  this.sex = sex;
  this.birthYear = birthYear;
  this.birthMonth = birthMonth;
  this.birthDay = birthDay;
  this.county = county;
  this.ordinal = ordinal;
  this.bornAbroad = bornAbroad;

  this.stringifySex = function() {
    let centuryBase = Math.floor(birthYear / 100) * 100;
    let maleDigit = undefined;
    switch(centuryBase) {
      case 1800:
        maleDigit = 3;
        break;
      case 1900:
        if(bornAbroad) {
          maleDigit = 7;
        } else {
          maleDigit = 1;
        }
        break;
      case 2000:
        maleDigit = 5;
    }
    if( sex === "m" ) {
      return "" + maleDigit;
    } else {
      return "" + (maleDigit + 1);
    }
  };

  this.stringify12 = function() {
    return this.stringifySex() +
      pad(birthYear % 100, 2) +
      pad(birthMonth, 2) +
      pad(birthDay, 2) +
      pad(county, 2) +
      pad(ordinal, 3);
  };

  this.stringify = stringify.bind(this);
  return this;
};

const cnpFromString = function(str) {
  if( str.length !== 13 ) {
    return null;
  }
  for( let i = 0; i < str.length; i++ ) {
    if( str.charAt(i) < '0' || str.charAt(i) > '9' ) {
      return null;
    }
  }
  if( checkDigit(str.substr(0, 12)) !== str.charAt(12) ) {
    return null;
  }

  let sex = undefined;
  switch(str[0]) {
    case '1':
    case '3':
    case '5':
    case '7':
      sex = "m";
      break;
    case '2':
    case '4':
    case '6':
    case '8':
      sex = "f";
      break;
    default:
      return null;
  }

  let centuryBase = undefined;
  switch(str[0]) {
    case '1':
    case '2':
    case '7':
    case '8':
      centuryBase = 1900;
      break;
    case '3':
    case '4':
      centuryBase = 1800;
      break;
    case '5':
    case '6':
      centuryBase = 2000;
      break;
    default:
      return null;
  }

  let bornAbroad = (str[0] === '7' || str[0] === '8');
  let birthYear = centuryBase + parseInt(str.substr(1, 2), 10);
  let birthMonth = parseInt(str.substr(3, 2), 10);
  let birthDay = parseInt(str.substr(5, 2), 10);
  if( ! isValidDate(birthYear, birthMonth, birthDay) ) {
    return null;
  }
  let county = parseInt(str.substr(7, 2), 10);
  let ordinal = parseInt(str.substr(9, 3), 10);

  return new CNP(sex, birthYear, birthMonth, birthDay, county, ordinal, bornAbroad);
};

const NIF = function(digits11) {
  this.digits11 = digits11;
  this.stringify12 = function() {
    return "9" + this.digits11;
  };
  this.stringify = stringify.bind(this);
  return this;
};

const nifFromString = function(str) {
  if( str.length !== 13 ) {
    return null;
  }
  for( let i = 0; i < str.length; i++ ) {
    if( str.charAt(i) < '0' || str.charAt(i) > '9' ) {
      return null;
    }
  }
  if( str.charAt(0) !== '9' ) {
    // Not a NIF
    return null;
  }
  if( checkDigit(str.substr(0, 12)) !== str.charAt(12) ) {
    return null;
  }
  return new NIF(str.substr(1, 11));
};

const isValid = function(str) {
  if(str.charAt(0) === '9') {
    return nifFromString(str) !== null;
  } else {
    return cnpFromString(str) !== null;
  }
};

exports.isValid = isValid;
exports.CNP = CNP;
exports.cnpFromString = cnpFromString;
exports.nifFromString = nifFromString;
