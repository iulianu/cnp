module Cnp

  # A NIF is kind of a CNP but without inner structure
  # and with the leading digit '9'.
  # The CNP check digit rules apply.
  class Nif
    extend IsValid::ClassMethods
    include CheckDigit::InstanceMethods

    def initialize(digits11)
      @digits11 = digits11
    end

    def stringify12
      "9" + @digits11
    end

    def self.from_string(s)
      return nil if s.size != 13
      return nil unless s =~ /^[0-9]+$/
      return nil unless s[0] == '9'
      return nil unless CheckDigit.check_digit(s[0...12]) == s[12]
      Nif.new(s[1...12])
    end

  end
end
