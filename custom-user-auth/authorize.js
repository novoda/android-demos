"use strict";

const Auth = require('./common/google-auth.js');
new Auth().startGoogleAuth().then(() => console.log("OAut2 tokens have been stored in " + CLIENT_SECRET_PATH));
