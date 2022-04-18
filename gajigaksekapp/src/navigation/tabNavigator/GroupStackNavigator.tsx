import React from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import GroupView from '../../screens/group/GroupView';
import GroupAddView from '../../screens/group/GroupAddView';
import GroupDetailView from '../../screens/group/GroupDetailView';
import GroupEditView from '../../screens/group/GroupEditView';
import FirbaseChatRoom from '../../services/firebaseAuth/FirebaseChatRoom';
import LectureDetailView from '../../screens/home/lecture/LectureDetailView';
import Profile from '../../screens/profile/Profile';
import PaymentView from '@/screens/payment/PaymentView';

const groupStack = createStackNavigator();

export default class GroupStackNavigator extends React.Component {
  render() {
    return (
      <groupStack.Navigator initialRouteName="groupList">
        <groupStack.Screen
          name="groupList"
          component={GroupView}
          options={{
            headerShown: false,
          }}
        />
        <groupStack.Screen
          name="groupAdd"
          component={GroupAddView}
          options={{
            headerShown: false,
          }}
        />
        <groupStack.Screen
          name="groupDashBoard"
          component={GroupDetailView}
          options={{
            headerShown: false,
          }}
        />
        <groupStack.Screen
          name="groupEdit"
          component={GroupEditView}
          options={{
            headerShown: false,
          }}
        />
        <groupStack.Screen
          name="chatroom"
          component={FirbaseChatRoom}
          options={{
            headerShown: false,
          }}
        />
        <groupStack.Screen
          name="lectureDash"
          component={LectureDetailView}
          options={{
            headerShown: false,
          }}
        />

        <groupStack.Screen
          name="profile"
          component={Profile}
          options={{
            headerShown: false,
          }}
        />

        <groupStack.Screen
          name="payment"
          component={PaymentView}
          options={{
            headerShown: false,
          }}
        />
      </groupStack.Navigator>
    );
  }
}
