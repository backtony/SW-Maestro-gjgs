import React from 'react';
import {StyleSheet} from 'react-native';
import {Text} from 'react-native';
import {SceneMap, TabBar, TabView} from 'react-native-tab-view';
import BulletinTab from './BulletinTab';
import GroupTab from './GroupTab';
import LectureTab from './LectureTab';

interface FavoriteTabViewProps {
  navigation: any;
  width: number;
}

export default class FavoriteTabView extends React.Component<
  FavoriteTabViewProps,
  {}
> {
  render() {
    return (
      <TabView
        renderTabBar={props => (
          <TabBar
            style={this.favoriteTabViewStyle.tabBar}
            indicatorStyle={[
              this.favoriteTabViewStyle.indicator,
              {marginHorizontal: (this.props.width / 3 - 40) / 2},
            ]}
            renderLabel={({route, focused, color}) => (
              <Text
                style={[
                  this.favoriteTabViewStyle.text,
                  focused
                    ? this.favoriteTabViewStyle.focused
                    : this.favoriteTabViewStyle.unfocused,
                ]}>
                {route.title}
              </Text>
            )}
            {...props}
          />
        )}
        style={this.favoriteTabViewStyle.tabView}
        navigationState={{
          index: 0,
          routes: [
            {key: 'class', title: '클래스'},
            {key: 'bulletin', title: '게시글'},
            {key: 'group', title: '그룹'},
          ],
        }}
        onIndexChange={index => {
          this.setState({tabIndex: index});
        }}
        renderScene={SceneMap({
          class: () => <LectureTab navigation={this.props.navigation} />,
          bulletin: () => <BulletinTab navigation={this.props.navigation} />,
          group: () => <GroupTab navigation={this.props.navigation} />,
        })}
      />
    );
  }

  private favoriteTabViewStyle = StyleSheet.create({
    tabBar: {backgroundColor: 'white'},
    indicator: {
      backgroundColor: '#4f6cff',
      height: 4,
      width: 40,
      alignSelf: 'center',
      borderRadius: 2,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
    focused: {color: '#4f6cff'},
    unfocused: {},
    tabView: {flex: 1},
  });
}
