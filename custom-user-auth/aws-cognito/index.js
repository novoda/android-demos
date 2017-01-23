"use strict";

// Change these values if necessary
const SPREADSHEET_ID = '1X0gPZ_2dyN0RaQlOk7P9unQfmWP9XF9jXXx9i9_iWxE';
const SPREADSHEET_RANGE = 'Sheet1!A2:D'; // 1 header row and then: user | password | token | identity id
const AWS_REGION = 'eu-west-1';
const AWS_IDENTITY_POOL_ID = 'eu-west-1:fb3e5ad5-d054-45f3-a01a-8eb63156d799';
const AWS_IDENTITY_PROVIDER_NAME = 'com.novoda.demo.customuserauth';

const CredentialsRepository = require('../common/credentials-repository.js');
const CognitoIdentityPool = require('./cognito-identity-pool.js');
const qs = require('querystring');

exports.handler = function (event, context, callback) {
  // Will need setting up API Gateway to pass in querystring params
  // as shown here http://stackoverflow.com/a/31857201/1180029
  const params = qs.parse(event.querystring);
  const username = params.username;
  const password = params.password;

  new CredentialsRepository(SPREADSHEET_ID, SPREADSHEET_RANGE)
    .fetch(username, password)
    .catch(err => {
      console.log("ERROR: " + err);
    }).then(user => new CognitoIdentityPool({
      region: AWS_REGION,
      identityPoolId: AWS_IDENTITY_POOL_ID,
      identityProviderName: AWS_IDENTITY_PROVIDER_NAME
    }).authenticate(user)).catch(err => {
      console.log("ERROR: " + err);
    }).then(result => {
      console.log("RESULT: " + JSON.stringify(result));
    });
}
