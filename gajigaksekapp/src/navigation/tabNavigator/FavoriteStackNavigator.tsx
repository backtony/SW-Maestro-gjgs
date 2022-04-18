import React from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import FirbaseChatRoom from '../../services/firebaseAuth/FirebaseChatRoom';
import LectureDetailView from '../../screens/home/lecture/LectureDetailView';
import FavoriteView from '../../screens/favorite/FavoriteView';

const favoriteStack = createStackNavigator();

export default class FavoriteStackNavigator extends React.Component {
  render() {
    return (
      <favoriteStack.Navigator initialRouteName="favoriteMain">
        <favoriteStack.Screen
          name="favoriteMain"
          component={FavoriteView}
          options={{
            headerShown: false,
          }}
        />
        <favoriteStack.Screen
          name="chatroom"
          component={FirbaseChatRoom}
          options={{
            headerShown: false,
          }}
        />
        <favoriteStack.Screen
          name="lectureDash"
          component={LectureDetailView}
          options={{
            headerShown: false,
          }}
        />
      </favoriteStack.Navigator>
    );
  }
}
