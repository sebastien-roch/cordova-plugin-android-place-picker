# Install

```
cordova plugin add git@github.com:sebastien-roch/cordova-plugin-android-place-picker.git
````

# Requirement
Google Play service. They will be required by the plugin. You will receive an error when calling the main function if the Google Play services are not present on the device.

# Usage

```
AndroidPlacePicker.pickPlace(onSuccess, onError);
```
The `onSuccess` function receives an object with the following structure:
```
{
    name: '18 Washington Avenue',
    coords: {
        longitude: '',
        latitude: ''
    }
}
```

The `onError` function receives an error message:
- `canceled`: the user cancelled the action (or pressed back)
- `Google Place service not present`: self descriptive
- `picker error. No connection?`: No internet connection available

# Licence

MIT