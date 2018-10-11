const CNP = require('./cnp.js');

describe("Suite", function () {
  it("validates Romanian personal number whether CNP or NIF", function () {
    expect(CNP.isValid("1690509049993")).toBe(true);
    expect(CNP.isValid("9000123456785")).toBe(true);
  });
  it("rejects invalid Romanian personal number whether CNP or NIF", function () {
    expect(CNP.isValid("1690509049990")).toBe(false);
  });
  it("parses CNP", function () {
    let cnp = CNP.cnpFromString("1690509049993");
    expect(cnp.sex).toBe("m");
    expect(cnp.birthYear).toBe(1969);
    expect(cnp.birthMonth).toBe(5);
    expect(cnp.birthDay).toBe(9);
    expect(cnp.county).toBe(4);
    expect(cnp.ordinal).toBe(999);
    expect(cnp.bornAbroad).toBe(false);
  });
  it("rejects CNP with wrong check digit", function () {
    expect(CNP.cnpFromString("1690509049990")).toBeNull();
  });
  it("rejects CNP with invalid date", function () {
    expect(CNP.cnpFromString("1691301049995")).toBeNull();
  });
  it("rejects ill-formed CNP", function () {
    expect(CNP.cnpFromString("1j91301049995")).toBeNull();
  });
  it("accepts CNP with a non-standard county", function () {
    /*
     * Typical values for the county field are 01-39, 41-46, and,
     * starting with 1981, also 51 and 52.
     * but CNPs with other values exist as well.
     */
    let cnp = CNP.cnpFromString("1690509990011");
    expect(cnp.sex).toBe("m");
    expect(cnp.birthYear).toBe(1969);
    expect(cnp.birthMonth).toBe(5);
    expect(cnp.birthDay).toBe(9);
    expect(cnp.county).toBe(99);
    expect(cnp.ordinal).toBe(1);
    expect(cnp.bornAbroad).toBe(false);
  });
  it("builds CNP from components", function () {
    let cnp = new CNP.CNP("m", 1969, 5, 9, 4, 999, false);
    expect(cnp.stringify()).toBe("1690509049993");
  });
  it("parses NIF", function () {
    let nif = CNP.nifFromString("9000123456785");
    expect(nif.stringify()).toBe("9000123456785");
  });
  it("rejects NIF with wrong check digit", function () {
    expect(CNP.nifFromString("9000123456780")).toBeNull();
  });
  it("rejects ill-formed NIF", function () {
    expect(CNP.nifFromString("9j91301049995")).toBeNull();
  });
});
