"use strict";

const Promise = require('bluebird');
const AWS = require('aws-sdk');

class CognitoIdentityPool {

  // params should have `region`, `identityPoolId` and `identityProviderName`.
  constructor(params) {
    this.params = params;
  }

  authenticate(user) {
    const params = {
      IdentityPoolId: this.params.identityPoolId,
      Logins: {}
    };
    params.Logins[this.params.identityProviderName] = user.username;
    return this._getOpenIdTokenFor(params);
  }

  _getOpenIdTokenFor(params) {
    return new Promise((resolve, reject) => {
      const identity = new AWS.CognitoIdentity({region:this.params.region});
      // Docs: http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/CognitoIdentity.html#getOpenIdTokenForDeveloperIdentity-property
      identity.getOpenIdTokenForDeveloperIdentity(params, (err, data) => {
        if (err) {
          throw new Error('Error while trying to retrieve open id for user ' + user + '. ' + err);
        }
        resolve(data);
      });
    });
  }

}

module.exports = CognitoIdentityPool;
