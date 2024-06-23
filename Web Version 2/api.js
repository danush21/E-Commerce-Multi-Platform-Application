const express = require('express');
const axios = require('axios');
const app = express();

const cors = require('cors');
app.use(cors());


app.get('/api/ebaydata', async (req, res) => {
  try {
    const baseUrl = 'https://svcs.ebay.com/services/search/FindingService/v1';

    const operationName = 'findItemsAdvanced';
    const serviceVersion = '1.0.0';
    const securityAppName = 'DanushGu-webtech0-PRD-294557f6d-e58fb9a5';
    const responseFormat = 'JSON';
    const entriesPerPage = 50;
    const keywords = req.query.keyword;
    const buyerPostalCode = req.query.from;
    const itemFilters = [];
    const outputSelectors = [];
    const maxDistance = req.query.distance;
    const freeShippingOnly = req.query.freeshipping;
    const localPickupOnly = req.query.localpickup;
    const hideDuplicateItems = true;
    const conditions = req.query.condition;
    const sellerInfo = true;
    const storeInfo = true;
    const page = req.query.page;

    if (maxDistance) {
      itemFilters.push({ name: 'MaxDistance', value: maxDistance });
    }
    if (freeShippingOnly) {
      itemFilters.push({ name: 'FreeShippingOnly', value: freeShippingOnly });
    }
    if (localPickupOnly) {
      itemFilters.push({ name: 'LocalPickupOnly', value: localPickupOnly });
    }
    if (hideDuplicateItems) {
      itemFilters.push({ name: 'HideDuplicateItems', value: hideDuplicateItems });
    }
    if (conditions && conditions.length > 0) {
      let s=''
      for (const condition of conditions) {
        if(condition==','){
          itemFilters.push({ name: 'Condition', value: s });
          s='';
        }
        else{
          s=s+condition;
        }
      }
      itemFilters.push({ name: 'Condition', value: s });
    }
    if (sellerInfo) {
      outputSelectors.push('SellerInfo');
    }
    if (storeInfo) {
      outputSelectors.push('StoreInfo');
    }
    let apiUrl = `${baseUrl}?OPERATION-NAME=${operationName}&SERVICE-VERSION=${serviceVersion}&SECURITY-APPNAME=${securityAppName}&RESPONSE-DATA-FORMAT=${responseFormat}&REST-PAYLOAD&paginationInput.entriesPerPage=${entriesPerPage}&keywords=${keywords}&buyerPostalCode=${buyerPostalCode}`;
    if (itemFilters.length > 0) {
      i=0;
      j=0;
      itemFilters.forEach((itemFilter, index) => {
        if(itemFilter.name=='Condition')
        {
          if(i==0)
          {
            j=index;
          }
          apiUrl += `&itemFilter(${j}).name=${itemFilter.name}&itemFilter(${j}).value(${i})=${itemFilter.value}`;
          i=i+1;
        }
        else{
          apiUrl += `&itemFilter(${index}).name=${itemFilter.name}&itemFilter(${index}).value=${itemFilter.value}`;
        }
      });
    if (outputSelectors.length > 0) {
      outputSelectors.forEach((outputSelector, index) => {
        apiUrl += `&outputSelector(${index})=${outputSelector}`;
      });
    }
    const response = await axios.get(apiUrl);

    res.json(response.data);

    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Failed to fetch eBay data' });
  }
});

//ProductInfoAPI

const OAuthToken = require('./ebay_oauth_token');

const client_id = 'client_id';
const client_secret = 'client_secret';

const oauthToken = new OAuthToken(client_id, client_secret);

oauthToken.getApplicationToken()
.then((accessToken) => {
    const headers = {
        'X-EBAY-API-IAF-TOKEN': accessToken,
    };

    app.get('/api/productinfo', async (req, res) => {
      try {

        const itemid = req.query.itemid;

        const apiUrl = `https://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=appid&siteid=0&version=967&ItemID=${itemid}&IncludeSelector=Description,Details,ItemSpecifics`;
    
        const response = await axios.get(apiUrl, {headers});
    
        res.json(response.data);
      } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Failed to fetch Product Info' });
      }
    });
})
.catch((error) => {
    console.error('Error obtaining access token:', error);
});



//PostalCodeAutoCompleteAPI

app.get('/api/postalcode', async (req, res) => {
  try {
    const apiUrl = `http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith=900&maxRows=5&username=danush21&country=US`;

    const response = await axios.get(apiUrl);

    res.json(response.data);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Failed to fetch Postal Codes' });
  }
});

//PhotosAPI

app.get('/api/photos', async (req, res) => {
  try {
    const q=req.query.q;
    const apiUrl = `https://www.googleapis.com/customsearch/v1?q=${q}&cx=b2aa7aa054e9646fe&imgSize=huge&imgType=photo&num=8&searchType=image&key=AIzaSyDIVQYHVIewtzinc9kM1lItlS8MUoGYMLM`;

    const response = await axios.get(apiUrl);

    res.json(response.data);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Failed to fetch Photos' });
  }
});

//SimilarItemsAPI

app.get('/api/similaritems', async (req, res) => {
  try {
    const itemid = req.query.itemid;
    const apiUrl = `https://svcs.ebay.com/MerchandisingService?OPERATION-NAME=getSimilarItems&SERVICE-NAME=MerchandisingService&SERVICE-VERSION=1.1.0&CONSUMER-ID=CONSUMER-ID&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&itemId=${itemid}&maxResults=20`;

    const response = await axios.get(apiUrl);

    res.json(response.data);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Failed to fetch Similar Items' });
  }
});

const port = 3000;
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
