require_relative 'cnp_generator'
require 'date'

gen = CnpGenerator.new
gen.generate(from_date: Date.new(1918,1,1), to_date: Date.new(1918,2,1)) do |cnp|
  puts cnp
end
