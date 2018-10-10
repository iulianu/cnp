require 'test/unit'
require_relative '../lib/cnp'

class NifTest < Test::Unit::TestCase

  def test_valid
    assert Cnp::Nif.valid?("9000123456785")
  end

  def test_invalid
    assert_false Cnp::Nif.valid?("9000123456780")
  end

  def test_construct_well_formed_nif
    nif = Cnp::Nif.from_string("9000123456785")
    assert_equal "9000123456785", nif.stringify()
  end

  def test_reject_nif_with_wrong_check_digit
    nif = Cnp::Nif.from_string("9000123456780")
    assert_nil nif
  end

  def test_reject_ill_formed_nif
    nif = Cnp::Nif.from_string("9j91301049995")
    assert_nil nif
  end

end