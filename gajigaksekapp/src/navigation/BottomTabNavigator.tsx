import * as React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import homeStack from './tabNavigator/HomeStackNavigator';
import MyPageStackNavigator from './tabNavigator/MyPageStackNavigator';
import Icon from 'react-native-vector-icons/Ionicons';
import GroupStackNavigator from './tabNavigator/GroupStackNavigator';
import FavoriteStackNavigator from './tabNavigator/FavoriteStackNavigator';
import CategoryStackNavigator from './tabNavigator/CategoryStackNavigator';
import {getFocusedRouteNameFromRoute} from '@react-navigation/native';
import {strJsonType} from '@/utils/Types';
import {Platform} from 'react-native';

const Tab = createBottomTabNavigator();

const noTabScreenName: Set<string> = new Set<string>([
  'groupAdd',
  'firstSubCategory',
  'firstLogin',
  'bulletinAdd',
  'lectureQuestionWrite',
  'lectureQuestionDash',
  'payment',
  'order',
]);

const tabIconName: strJsonType = {
  home: 'ios-home-sharp',
  category: 'ios-menu-sharp',
  group: 'ios-people',
  favorite: 'ios-heart',
  my: 'ios-person',
};

class BottomTabNavigator extends React.Component {
  private getTabBarVisibility = route => {
    const routeName = getFocusedRouteNameFromRoute(route);
    return !(routeName && noTabScreenName.has(routeName));
  };

  render() {
    return (
      <Tab.Navigator
        screenOptions={({route}) => ({
          tabBarIcon: ({focused, color, size}) => {
            return (
              <Icon
                name={
                  route.name
                    ? tabIconName[route.name]
                    : 'ios-information-circle'
                }
                size={size}
                color={color}
              />
            );
          },
        })}
        tabBarOptions={{
          labelStyle: {
            fontSize: 10,
          },
        }}>
        <Tab.Screen
          name="home"
          component={homeStack}
          options={({route}) => ({
            tabBarVisible: this.getTabBarVisibility(route),
            tabBarLabel: '홈',
          })}
        />
        <Tab.Screen
          name="category"
          component={CategoryStackNavigator}
          options={({route}) => ({
            tabBarVisible: this.getTabBarVisibility(route),
            tabBarLabel: '카테고리',
          })}
        />
        <Tab.Screen
          name="group"
          component={GroupStackNavigator}
          options={({route}) => ({
            tabBarVisible: this.getTabBarVisibility(route),
            tabBarLabel: '그룹',
          })}
        />
        <Tab.Screen
          name="favorite"
          component={FavoriteStackNavigator}
          options={({route}) => ({
            tabBarVisible: this.getTabBarVisibility(route),
            tabBarLabel: '찜',
          })}
        />
        <Tab.Screen
          name="my"
          component={MyPageStackNavigator}
          options={({route}) => ({
            tabBarVisible: this.getTabBarVisibility(route),
            tabBarLabel: '마이',
            unmountOnBlur: Platform.OS === 'ios' ? false : true,
          })}
        />
      </Tab.Navigator>
    );
  }
}

export default BottomTabNavigator;
