require_relative 'category'

class CategoryRepository
  attr_reader :categories

  def initialize
    @categories = [
        Category.new(1,'SHIRTS', 'shirtimage'),
        Category.new(2,'HOODIES', 'hoodieimage'),
        Category.new(3, 'HATS', 'hatimage'),
        Category.new(4,'DIGITAL GOODS', 'digitalgoodsimage'),
        Category.new(5, 'something', 'shirtimage')
    ]
  end
end