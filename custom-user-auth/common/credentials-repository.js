const Promise = require('bluebird');
const Auth = require('./google-auth.js');

class CredentialsRepository {

  constructor(spreadsheetId, spreadsheetRange) {
    this.spreadsheetId = spreadsheetId;
    this.spreadsheetRange = spreadsheetRange;
    this.sheetsClient = require('googleapis').sheets('v4');
    Promise.promisifyAll(this.sheetsClient.spreadsheets.values);
  }

  fetch(user, password) {
    return new Auth().startGoogleAuth()
      .then(oauth2Client => this._fetchSpreadsheetValues(this.sheetsClient, oauth2Client))
      .then(data => data.values)
      .then(values => this._findMatchingUserToken(values, user, password));
  }

  _fetchSpreadsheetValues(sheetsClient, oauth2Client) {
    return sheetsClient.spreadsheets.values.getAsync({
      spreadsheetId: this.spreadsheetId,
      range: this.spreadsheetRange,
      auth: oauth2Client
    });
  }

  _findMatchingUserToken(values, user, pass) {
    const matchingUser = values
      .map(row => ({
        username: row[0],
        password: row[1],
        token: row[2],
        identityId: row[3],
      }))
      .filter(sheetUserInfo => this._validateCredentials(sheetUserInfo, user, pass));
    if (matchingUser.length === 0) {
      throw new Error('No matching users');
    }
    return Promise.resolve({
      username: matchingUser[0].username,
      token: matchingUser[0].token,
    });
  }

  _validateCredentials(sheetUserInfo, user, pass) {
    return sheetUserInfo.username === user && sheetUserInfo.password === pass;
  }

}

module.exports = CredentialsRepository;
