require 'sinatra' 
require 'json'
require 'rubygems'
require 'rufus-scheduler'
require_relative 'category_repository'
require_relative 'item_repository'
require_relative 'basket'

@@categories = CategoryRepository.new.categories
@@item_repository = ItemRepository.new
@@basket = Basket.new

before do
  content_type :json
end


get '/category/:number' do
  {category: @@categories[params['number'].to_i]}
end

get '/categories' do
  {categories: @@categories}
end

get '/category/:id/items' do
  category = params['id'].to_sym
  {products: @@item_repository.items_from_category(category)}
end

get '/items/:id/add' do
  @@basket.add_to_basket(@@item_repository.get_item_for_id(params['id']))
end

get '/items/:id/remove' do
  @@basket.remove_from_basket(@@item_repository.get_item_for_id(params['id']))
end

get '/basket' do
  {basket: @@basket.show_content}
end

get '/items/:id' do
  {item: @@item_repository.get_item_for_id(params['id'])}
end


after do
  response.body = JSON.dump(response.body)
end
