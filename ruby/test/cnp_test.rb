require 'test/unit'
require_relative '../lib/cnp'

class CnpTest < Test::Unit::TestCase

  def test_valid
    assert Cnp::Cnp.valid?("1690509049993")
  end

  def test_invalid
    assert_false Cnp::Cnp.valid?("1690509049990")
  end

  def test_parse_cnp
    cnp = Cnp::Cnp.from_string("1690509049993")
    assert_equal :m, cnp.sex
    assert_equal Date.new(1969,5,9), cnp.date
    assert_equal 4, cnp.county
    assert_equal 999, cnp.ordinal
    assert_equal false, cnp.born_abroad
  end

  def test_reject_cnp_with_wrong_check_digit
    cnp = Cnp::Cnp.from_string("1690509049990")
    assert_nil cnp
  end

  def test_reject_cnp_with_invalid_date
    cnp = Cnp::Cnp.from_string("1691301049995")
    assert_nil cnp
  end

  def test_reject_ill_formed_cnp
    cnp = Cnp::Cnp.from_string("1j91301049995")
    assert_nil cnp
  end

  # Typical values for the county field are 01-39, 41-46, and,
  # starting with 1981, also 51 and 52.
  # but CNPs with other values exist as well.
  def test_accept_cnp_nonstandard_county
    cnp = Cnp::Cnp.from_string("1690509990011")
    assert_equal :m, cnp.sex
    assert_equal Date.new(1969,5,9), cnp.date
    assert_equal 99, cnp.county
    assert_equal 1, cnp.ordinal
    assert_equal false, cnp.born_abroad
  end

  def test_build_cnp
    cnp = Cnp::Cnp.new(:m, Date.new(1969, 5, 9), 4, 999, false)
    assert_equal "1690509049993", cnp.stringify()
  end

end
