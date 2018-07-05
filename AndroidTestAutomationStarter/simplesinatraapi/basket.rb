class Basket

  def initialize
    @items_in_basket = {}
  end

  def reset_basket
    @items_in_basket = {}
  end

  def add_to_basket(item)
    item_id = item.id
    p item_id
    if @items_in_basket.key?(item_id)
      @items_in_basket[item_id] += 1
    else
      @items_in_basket.merge!({item_id => 1})
    end
  end

  def remove_from_basket(item)
    item_id = item.id
    if @items_in_basket.key?(item_id)
      if @items_in_basket[item_id] > 1
        @items_in_basket[item_id] -= 1
      else
        @items_in_basket.delete(item_id)
      end
    end
  end

  def show_content
    @items_in_basket
  end

end