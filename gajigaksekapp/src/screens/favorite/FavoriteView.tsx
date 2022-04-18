import React from 'react';
import {View, StyleSheet} from 'react-native';
import FavoriteViewHeader from './components/FavoriteViewHeader';
import FavoriteTabView from './components/favoriteTabView./FavoriteTabView';

interface FavoriteViewStates {
  width: number;
}

export default class FavoriteView extends React.Component<
  {},
  FavoriteViewStates
> {
  constructor() {
    super();
    this.state = {
      width: 0,
    };
  }

  render() {
    return (
      <View style={this.styles.container}>
        <FavoriteViewHeader title={'찜한 클래스'} />

        <View
          style={this.styles.innerContainber}
          onLayout={event => {
            var {width} = event.nativeEvent.layout;
            this.setState({width});
          }}>
          <FavoriteTabView
            navigation={this.props.navigation}
            width={this.state.width}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
    innerContainber: {backgroundColor: '#FFF', flex: 1},
  });
}
