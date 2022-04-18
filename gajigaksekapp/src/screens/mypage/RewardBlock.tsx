import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {RewardInfo} from './RewardView';

export default class RewardBlock extends React.Component<RewardInfo, {}> {
  private parseDate(date: string) {
    return `${date.substr(2, 2)}.${date.substr(5, 2)}.${date.substr(8, 2)}`;
  }
  render() {
    return (
      <View>
        <View style={this.styles.wrapper}>
          <View>
            <Text style={this.styles.text}>{this.props.text}</Text>
            <Text style={this.styles.text2}>
              {this.parseDate(this.props.createdDate)}
            </Text>
          </View>
          <View>
            <Text
              style={[
                this.styles.text3,
                this.props.rewardType === 'SAVE' ? {} : {color: '#ff4f4f'},
              ]}>
              {this.props.rewardType === 'SAVE'
                ? `+${this.props.amount}`
                : `-${this.props.amount}`}
            </Text>
          </View>
        </View>
        <View style={{width: 320, height: 1, backgroundColor: '#f5f5f7'}} />
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      height: 80,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 16,
      fontWeight: '500',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      color: '#111111',
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#8e8e8f',
    },
    text3: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      textAlign: 'right',
      color: '#4f6cff',
    },
  });
}
