import * as React from 'react';
import {View, StyleSheet} from 'react-native';
import GroupItemButtonContainer from './GroupItemButtonContainer';
import GroupItemContent from './GroupItemContent';

export default class GroupItem extends React.Component<
  {
    title: string;
    subCategory: string;
    isLeader: boolean;
    apply: number;
    all: number;
    onPress: () => void;
    onPressChat: () => void;
    onPressList: () => void;
  },
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <GroupItemContent
          onPress={() => this.props.onPress()}
          title={this.props.title}
          apply={this.props.apply}
          all={this.props.all}
          subCategory={this.props.subCategory}
        />
        <GroupItemButtonContainer
          onPressChat={() => this.props.onPressChat()}
          onPressList={() => this.props.onPressList()}
          isLeader={this.props.isLeader}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      padding: 20,
      borderBottomColor: '#f5f5f7',
      borderBottomWidth: 1,
    },
  });
}
