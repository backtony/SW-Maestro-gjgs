/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';
import type {Node} from 'react';
import {Alert, Linking, LogBox, Platform, useColorScheme} from 'react-native';

import {NavigationContainer} from '@react-navigation/native';
import BottomTabNavigator from './navigation/BottomTabNavigator';
import messaging from '@react-native-firebase/messaging';
import {navigate, navigationRef} from './navigation/RootNavigation';
import NotificationController from './services/apis/NotificationController';

async function requestUserPermission() {
  const authorizationStatus = await messaging().requestPermission();

  if (authorizationStatus) {
    console.log('Permission status:', authorizationStatus);
  }
}

const App: () => Node = () => {
  messaging().setBackgroundMessageHandler(async remoteMessage => {
    console.log('Message handled in the background!', remoteMessage);
  });

  messaging().onNotificationOpenedApp(async remoteMessage => {
    console.log(
      'Notification caused app to open from background state:',
      remoteMessage.notification,
    );
    if (remoteMessage.data) {
      const res = await NotificationController.postRead(
        remoteMessage.data.uuid,
      );
      console.log('postRead res: ', res);
    }
  });

  React.useEffect(() => {
    if (Platform.OS === 'android') {
      Linking.getInitialURL().then(url => {
        console.log(url);
        console.log('good');
        // this.navigate(url);
      });
    } else {
      Linking.addEventListener('url', (obj: any) => {
        const url: string = obj.url;

        const route = url.replace(/.*?:\/\//g, '');
        const splitList = route.split('/');
        console.log('route: ', route);

        if (splitList[0] === 'lecture') {
          console.log('lecture: ', splitList[1]);
          navigate('lectureDash', {
            lectureId: +splitList[1],
          });
        } else if (splitList[0] === 'bulletin') {
          console.log('bulletin: ', splitList[1]);
          navigate('bulletinDash', {
            bulletinId: +splitList[1],
            myFavorite: false,
            refreshParent: () => {},
          });
        } else if (splitList[0] === 'chatroom') {
          console.log('chatroom: ', splitList[1]);
          navigate('chatroom', {
            thread: {
              teamId: splitList[2],
              teamName: splitList[3],
              onPressAvatar: (memberId: number) => {
                navigate('profile', {memberId});
              },
            },
          });
        }
      });
      // this.props.navigation.navigate('lectureDash', {
      //   lectureId: this.props.team.bulletinsLecture.lectureId,
      // });
      console.log('??!');
      // Linking.addEventListener('url', this.handleOpenURL);

      //로그박스 활성 및 비활성화
      LogBox.ignoreAllLogs();
    }

    requestUserPermission();
    const unsubscribe = messaging().onMessage(async remoteMessage => {
      console.log('fcm msg: ', remoteMessage);
      Alert.alert(
        remoteMessage.notification && remoteMessage.notification.title
          ? remoteMessage.notification.title
          : 'title',
        remoteMessage.notification && remoteMessage.notification.body
          ? remoteMessage.notification.body
          : 'body',
      );
      if (remoteMessage.data) {
        const res = await NotificationController.postRead(
          remoteMessage.data.uuid,
        );
        console.log('postRead res: ', res);
      }
    });
    return unsubscribe;
  });

  return (
    <NavigationContainer ref={navigationRef}>
      <BottomTabNavigator />
    </NavigationContainer>
  );
};

export default App;
