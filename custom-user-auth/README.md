#custom-user-auth

##Pre-requisites

Have `nodejs` and `npm` installed.

##Usage

0. Clone the repo.
1. Follow steps `a` to `h` described [here](https://developers.google.com/sheets/api/quickstart/nodejs#step_1_turn_on_the_api_name).
2. Move the `client_secret.json` file to the `common/` folder.
3. Run `npm install`.
4. Run `node authorize.js`
5. When prompted, visit the URL printed in the terminal, then press the 'Allow' button, and copy the resulting code and paste it to the console.

### AWS Cognio

1. Once authorized, run `cd aws-cognito` and edit the constants in `index.js`.
2. Start the app `npm start`
3. This should authenticate a user defined in `aws-cognito/event.json` using the spreadsheet and a Identity Pool from AWS Cognito.
