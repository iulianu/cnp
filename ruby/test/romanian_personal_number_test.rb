require 'test/unit'
require_relative '../lib/cnp'

class RomanianPersonalNumberTest < Test::Unit::TestCase
  def test_create_cnp
    number = Cnp::RomanianPersonalNumber.from_string("1690509049993")
    assert number.kind_of?(Cnp::Cnp)
  end

  def test_create_nif
    number = Cnp::RomanianPersonalNumber.from_string("9000123456785")
    assert number.kind_of?(Cnp::Nif)
  end

  def test_valid
    assert Cnp::RomanianPersonalNumber.valid?("1690509049993")
  end

  def test_invalid
    assert_false Cnp::RomanianPersonalNumber.valid?("1690509049990")
  end
end