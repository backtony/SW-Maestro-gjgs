import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import GeneralButton from '../../../../../components/GeneralButton';

interface GroupPeopleBlockProps {
  setPeople: (people: number) => void;
  people: number;
}

export default class GroupPeopleBlock extends React.Component<
  GroupPeopleBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.container}>인원</Text>
        <View style={this.styles.innerContainer}>
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
    container: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    innerContainer: {flexDirection: 'row'},
  });
}
