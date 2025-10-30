const amplifyconfig = '''
{
  "UserAgent": "aws-amplify-cli/2.0",
  "Version": "1.0",
  "auth": {
    "plugins": {
      "awsCognitoAuthPlugin": {
        "UserAgent": "aws-amplify-cli/0.1.0",
        "Version": "0.1.0",
        "IdentityManager": {
          "Default": {}
        },
        "CognitoUserPool": {
          "Default": {
            "PoolId": "eu-north-1_ExI0Aq7Ov",
            "AppClientId": "3fcub95ivg1a9ib76gk7ef35fh",
            "Region": "eu-north-1"
          }
        },
        "Auth": {
          "Default": {
            "authenticationFlowType": "USER_SRP_AUTH",
            "socialProviders": [],
            "usernameAttributes": ["EMAIL"],
            "signupAttributes": ["EMAIL", "NAME"],
            "passwordProtectionSettings": {
              "passwordPolicyMinLength": 8,
              "passwordPolicyCharacters": []
            },
            "mfaConfiguration": "OPTIONAL",
            "mfaTypes": ["TOTP", "SMS"],
            "verificationMechanisms": ["EMAIL"]
          }
        }
      }
    }
  }
}
''';
