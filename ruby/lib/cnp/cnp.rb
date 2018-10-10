require 'date'
require_relative 'is_valid'
require_relative 'check_digit'

module Cnp
  class Cnp
    extend IsValid::ClassMethods
    include CheckDigit::InstanceMethods

#    VALID_COUNTIES = (1..46).to_a + [51, 52]
    MIN_BIRTH_DATE = Date.new(1800,1,1)
    MAX_BIRTH_DATE = Date.new(2099,12,31)

    attr_reader :sex, :date, :county, :ordinal, :born_abroad

    def initialize(sex, date, county, ordinal, born_abroad)
      raise "Incorrect sex" unless [:m, :f].include?(sex)
      if (date < Cnp::MIN_BIRTH_DATE) || (date > Cnp::MAX_BIRTH_DATE)
        raise "Birth date out of range"
      end
      if born_abroad && (date.year < 1900 || date.year > 1999)
        raise "Birth date out of range for people born abroad"
      end
#      raise "Incorrect county" unless Cnp::VALID_COUNTIES.include?(county)
      if (ordinal < 0 || ordinal > 999)
        raise "Ordinal out of range"
      end
      @sex = sex
      @date = date
      @county = county
      @ordinal = ordinal
      @born_abroad = born_abroad
    end

    def stringify12
      stringify_sex + stringify_date + format("%02d%03d", @county, @ordinal)
    end

    def stringify_sex
      male_digit = case century
                   when 1800 then 3
                   when 1900 then (@born_abroad ? 7 : 1)
                   when 2000 then 5
                   end
      case sex
      when :m then male_digit
      when :f then male_digit + 1
      end.to_s
    end

    def century
      @date.year / 100 * 100
    end

    def stringify_date
      format("%02d%02d%02d", @date.year % 100, @date.month, @date.day)
    end

    def self.from_string(s)
      # TODO should collect errors
      return nil if s.size != 13
      return nil unless s =~ /^[0-9]+$/
      return nil unless CheckDigit.check_digit(s[0...12]) == s[12]
      sex = case s[0]
            when '1', '3', '5', '7' then :m
            when '2', '4', '6', '8' then :f
            else return nil
            end
      century_base = case s[0]
                     when '1', '2', '7', '8' then 1900
                     when '3', '4' then 1800
                     when '5', '6' then 2000
                     end
      born_abroad = (s[0] == '7' || s[0] == '8')
      date = begin
               Date.new(century_base + s[1..2].to_i, s[3..4].to_i, s[5..6].to_i)
             rescue ArgumentError
               return nil
             end
      county = s[7..8].to_i
      ordinal = s[9..11].to_i
#      return nil unless VALID_COUNTIES.include?(county)

      Cnp.new(sex, date, county, ordinal, born_abroad)
    end

  end
end
