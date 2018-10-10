module Cnp
  # Relies on .from_string on the mixin target
  module IsValid
    module ClassMethods
      def valid?(str)
        self.from_string(str) != nil
      end
    end
  end
end