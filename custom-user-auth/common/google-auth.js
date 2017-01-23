"use strict";

const SCOPES = ['https://www.googleapis.com/auth/spreadsheets.readonly'];
const TOKEN_DIR = __dirname + '/.credentials/';
const TOKEN_PATH = TOKEN_DIR + 'google-sheets-tokens.json';
const CLIENT_SECRET_PATH = __dirname + '/client_secret.json'

const fs = require('fs-promise');
const readline = require('readline');
const googleAuth = require('google-auth-library');
const Promise = require('bluebird');

class Auth {

  startGoogleAuth() {
    return fs.readFile(CLIENT_SECRET_PATH)
      .then(content => Promise.resolve(JSON.parse(content)))
      .then(jsonContent => this._authorize(jsonContent));
  }

  _authorize(credentials) {
    const clientSecret = credentials.installed.client_secret;
    const clientId = credentials.installed.client_id;
    const redirectUrl = credentials.installed.redirect_uris[0];
    const auth = new googleAuth();
    const oauth2Client = new auth.OAuth2(clientId, clientSecret, redirectUrl);

    return fs.readFile(TOKEN_PATH)
      .catch(error => this._getNewToken(oauth2Client))
      .then((token) => {
        oauth2Client.credentials = JSON.parse(token);
        return Promise.resolve(oauth2Client);
      });
  }

  _getNewToken(oauth2Client) {
    const authUrl = oauth2Client.generateAuthUrl({
      access_type: 'offline',
      scope: SCOPES
    });
    console.log('Authorize this app by visiting this url: ', authUrl);

    return this._promisifiedQuestion('Enter the code from that page here: ')
      .then(result => this._promisifiedGetToken(oauth2Client, result))
      .then(token => this._storeToken(token));
  }

  _promisifiedQuestion(message) {
    this.rl = readline.createInterface({
      input: process.stdin,
      output: process.stdout
    });
    return new Promise((resolve, reject) => {
      this.rl.question(message, (code) => {
        this.rl.close();
        resolve(code);
      });
    });
  }

  _promisifiedGetToken(oauth2Client, code) {
    return new Promise((resolve, reject) => {
      oauth2Client.getToken(code, (err, token) => {
        if (err) {
          throw new Error('Error while trying to retrieve access token. ' + err);
        }
        oauth2Client.credentials = token;
        resolve(token);
      })
    });
  }

  _storeToken(token) {
    return fs.mkdirs(TOKEN_DIR)
      .then(fs.writeFile(TOKEN_PATH, JSON.stringify(token)))
      .then(Promise.resolve(token));
  }

}

module.exports = Auth;
