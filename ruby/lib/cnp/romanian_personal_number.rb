module Cnp
  class RomanianPersonalNumber
    extend IsValid::ClassMethods

    def self.from_string(s)
      if s[0] == '9'
        Nif.from_string(s)
      else
        Cnp.from_string(s)
      end
    end
  end
end