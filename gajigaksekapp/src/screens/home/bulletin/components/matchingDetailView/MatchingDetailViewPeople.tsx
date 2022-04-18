import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface MatchingDetailViewPeopleProps {
  people: number;
  setPeople: (people: number) => void;
}

export default class MatchingDetailViewPeople extends React.Component<
  MatchingDetailViewPeopleProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>인원</Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'2인'}
            onClick={() => {
              this.props.setPeople(2);
            }}
            clicked={this.props.people === 2}
          />
          <GeneralButton
            title={'3인'}
            onClick={() => {
              this.props.setPeople(3);
            }}
            clicked={this.props.people === 3}
          />
          <GeneralButton
            title={'4인'}
            onClick={() => {
              this.props.setPeople(4);
            }}
            clicked={this.props.people === 4}
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
