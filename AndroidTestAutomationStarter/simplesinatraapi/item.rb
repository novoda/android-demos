class Item
  attr_accessor :id, :title, :price, :image, :product_description

  def initialize(id, title, price, image, product_description)
    @id = id
    @title = title
    @price = price
    @image = image
    @product_description = product_description
  end

  def as_json(options={})
    {
        id: @id,
        title: @title,
        image: @image,
        price: @price,
        product_description: @product_description
    }
  end

  def to_json(*options)
    as_json(*options).to_json(*options)
  end
end

