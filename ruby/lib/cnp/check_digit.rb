module Cnp

  # Relies on #stringify12() from the mixin target
  module CheckDigit

    module InstanceMethods

      def stringify
        s12 = self.stringify12()
        s12 + CheckDigit.check_digit(s12)
      end

    end

    CHECK_DIGIT_FACTORS = "279146358279"

    def self.check_digit(s12)
      s = 0
      for i in 0...12
        s += s12[i].to_i * CHECK_DIGIT_FACTORS[i].to_i
      end
      s %= 11
      if s == 10
        1
      else
        s
      end.to_s
    end

  end
end