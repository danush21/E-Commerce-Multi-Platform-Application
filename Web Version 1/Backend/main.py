from flask import Flask, request, jsonify, render_template
import requests
from flask_cors import CORS
from ebay_oauth_token import OAuthToken

app = Flask(__name__)
CORS(app)

EBAY_API_KEY = 'DanushGu-webtech0-PRD-294557f6d-e58fb9a5'
client_secret = "PRD-94557f6d0f0a-a0a3-40c6-abdc-8982"

oauth_utility = OAuthToken(EBAY_API_KEY, client_secret)
application_token = oauth_utility.getApplicationToken()

headers = {
"X-EBAY-API-IAF-TOKEN": oauth_utility.getApplicationToken()
}

EBAY_API_URL = 'https://svcs.ebay.com/services/search/FindingService/v1'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/search', methods=['GET'])
def search_ebay():
    query = request.args.get('query')
    sort_order = request.args.get('sort_order', 'BestMatch')
    price_range_from = request.args.get('price_range_from','')
    price_range_to = request.args.get('price_range_to','')
    seller_returns_accepted = request.args.get('seller_returns_accepted', '')
    free_shipping = request.args.get('free_shipping', '')
    condition_values = request.args.getlist('condition')

    payload = {
        'OPERATION-NAME': 'findItemsAdvanced',
        'SERVICE-VERSION': '1.0.0',
        'SECURITY-APPNAME': EBAY_API_KEY,
        'RESPONSE-DATA-FORMAT': 'JSON',
        'REST-PAYLOAD': True,
        'keywords': query,
        'sortOrder': sort_order,
        'paginationInput.entriesPerPage': 10,
        'paginationInput.pageNumber': 1,
    }
    
    j = 0

    if price_range_from:
        payload[f'itemFilter({j}).name'] = 'MinPrice'
        payload[f'itemFilter({j}).value'] = price_range_from
        payload[f'itemFilter({j}).paramName'] = 'Currency'
        payload[f'itemFilter({j}).paramValue'] = 'USD'
        j += 1

    if price_range_to:
        payload[f'itemFilter({j}).name'] = 'MaxPrice'
        payload[f'itemFilter({j}).value'] = price_range_to
        payload[f'itemFilter({j}).paramName'] = 'Currency'
        payload[f'itemFilter({j}).paramValue'] = 'USD'
        j += 1

    if seller_returns_accepted:
        payload[f'itemFilter({j}).name'] = 'ReturnsAcceptedOnly'
        payload[f'itemFilter({j}).value'] = seller_returns_accepted
        j += 1

    if free_shipping:
        payload[f'itemFilter({j}).name'] = 'FreeShippingOnly'
        payload[f'itemFilter({j}).value'] = free_shipping
        j += 1

    if condition_values:
        for i, condition_value in enumerate(condition_values):
            payload[f'itemFilter({j}).name'] = 'Condition'
            payload[f'itemFilter({j}).value({i})'] = condition_value


    response_data = {}
    
    try:
        response = requests.get(EBAY_API_URL, params=payload)
        response.raise_for_status()
        data = response.json()

        items = []

        if 'findItemsAdvancedResponse' in data:
            search_result = data['findItemsAdvancedResponse'][0]

            if 'paginationOutput' in search_result:
                total_results = int(search_result['paginationOutput'][0]['totalEntries'][0])

                if 'searchResult' in search_result:
                    search_items = search_result['searchResult'][0].get('item', [])

                    for item in search_items:
                        title = item.get('title', '')
                        price = item.get('sellingStatus', [{}])[0].get('currentPrice', [{}])[0].get('__value__', '')
                        currency = item.get('sellingStatus', [{}])[0].get('currentPrice', [{}])[0].get('@currencyId', '')
                        url = item.get('viewItemURL', '')
                        categories = item.get('primaryCategory', [])
                        category_name = categories[0].get('categoryName', '')
                        conditions = item.get('condition', [])
                        itemId = item.get('itemId')

                        if 'condition' in item:
                            conditions = item['condition']

                            if isinstance(conditions, list) and len(conditions) > 0:
                                condition_name = conditions[0].get('conditionDisplayName', '')
                            else:
                                condition_name = ''
                        else:
                            condition_name = ''

                        shipping_info = item.get('shippingInfo', [])
                        shipping_cost = shipping_info[0].get('shippingServiceCost', [{}])[0].get('__value__', '')
                        image_url = item.get('galleryURL', 'https://csci571.com/hw/hw6/images/eBayLogo.png')
                        top_rated = item.get('topRatedListing')[0]

                        item_data = {
                            'title': title,
                            'price': price,
                            'currency': currency,
                            'url': url,
                            'category': category_name,
                            'condition': condition_name,
                            'shipping_cost': shipping_cost,
                            'image_url': image_url,
                            'top_rated': top_rated,
                            'itemId': itemId
                        }
                        
                        items.append(item_data)
                        response_data = {
                            'total_results': total_results,
                            'items': items
                        }

                else:
                    items = []

                    response_data = {
                        'total_results': 0,
                        'items': []
                    }

    except requests.exceptions.RequestException as e:
        return jsonify({'error': 'Error fetching data from eBay API'}), 500

    response = jsonify(response_data)
    response.headers['Content-Type'] = 'application/json'
    return response

@app.route('/getitem')
def get_item():
    query = request.args.get('query')

    gsi_api_url = 'https://open.api.ebay.com/shopping'
    params = {
        'callname': 'GetSingleItem',
        'responseencoding': 'JSON',
        'appid': EBAY_API_KEY,
        'siteid': '0',
        'version': '967',
        'ItemID': query,
        'IncludeSelector': 'Description,Details,ItemSpecifics'
    }

    response = requests.get(gsi_api_url, headers=headers, params=params)

    if response.status_code == 200:
        ebay_data = response.json()

        items = []
        if 'Item' in ebay_data:
            item = ebay_data.get('Item', {})
            image_url = item.get('PictureURL', ['https://csci571.com/hw/hw6/images/eBayLogo.png'])[0]
            link = item.get('ViewItemURLForNaturalSearch', '')
            title = item.get('Title', '')
            price = item.get('ConvertedCurrentPrice', {}).get('Value', 0)
            location = item.get('Location', '')
            seller = item.get('Seller', {}).get('UserID', '')
            return_policy = item.get('ReturnPolicy', {})
            item_specifics = item.get('ItemSpecifics', {}).get('NameValueList', [])

            item_data = {
                'image_url': image_url,
                'link': link,
                'title': title,
                'price': price,
                'location': location,
                'seller': seller,
                'return_policy': return_policy,
                'item_specifics': item_specifics
                
            }

            items.append(item_data)

            response = jsonify(items)
            response.headers['Content-Type'] = 'application/json'
            return response

        else:
            items = []
            response = jsonify(items)
            response.headers['Content-Type'] = 'application/json'
            return response

    else:
        return jsonify({'error': 'Error while fetching data from eBay API'}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8080, debug=True)