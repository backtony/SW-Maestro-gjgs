import React from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import CategoryView from '../../screens/category/CategoryView';
import LectureSearchResultView from '../../screens/home/lecture/LectureSearchResultView';

const categoryStack = createStackNavigator();

class CategoryStackNavigator extends React.Component {
  render() {
    return (
      <categoryStack.Navigator initialRouteName="mainCategory">
        <categoryStack.Screen
          name="mainCategory"
          component={CategoryView}
          options={{
            headerShown: false,
          }}
        />

        <categoryStack.Screen
          name="lectureSearchResult"
          component={LectureSearchResultView}
          options={{
            headerShown: false,
          }}
        />
      </categoryStack.Navigator>
    );
  }
}

export default CategoryStackNavigator;
