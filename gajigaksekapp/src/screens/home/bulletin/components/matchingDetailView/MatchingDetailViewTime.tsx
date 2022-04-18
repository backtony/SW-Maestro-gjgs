import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface MatchingDetailViewTimeProps {
  time: string;
  setTime: (time: string) => void;
}

export default class MatchingDetailViewTime extends React.Component<
  MatchingDetailViewTimeProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>가능한 시간</Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'오전'}
            onClick={() => {
              this.props.setTime('MORNING');
            }}
            clicked={this.props.time === 'MORNING'}
          />
          <GeneralButton
            title={'오후'}
            onClick={() => {
              this.props.setTime('NOON');
            }}
            clicked={this.props.time === 'NOON'}
          />
          <GeneralButton
            title={'저녁'}
            onClick={() => {
              this.props.setTime('AFTERNOON');
            }}
            clicked={this.props.time === 'AFTERNOON'}
          />
        </View>
      </View>
    );
  }
  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
  });
}
