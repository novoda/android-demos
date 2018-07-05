require 'json'

class Category
  attr_reader :id, :title, :image

  def initialize(id, title, image)
    @id = id
    @title = title
    @image = image
  end

  def as_json(options={})
    {
        id: @id,
        title: @title,
        image: @image
    }
  end

  def to_json(*options)
    as_json(*options).to_json(*options)
  end
end