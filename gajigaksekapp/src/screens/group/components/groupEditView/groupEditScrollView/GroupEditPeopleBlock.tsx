import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {Text, View} from 'react-native';

interface GroupEditPeopleBlockProps {
  setPeople: (people: string) => void;
  people: string;
}

export default class GroupEditPeopleBlock extends React.Component<
  GroupEditPeopleBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text
          style={{
            fontSize: 14,
            lineHeight: 16,
            fontFamily: 'NotoSansCJKkr-Regular',
            marginTop: 20,
            marginBottom: 10,
          }}>
          인원
        </Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'2인'}
            onClick={() => {
              this.props.setPeople('2인');
            }}
            clicked={this.props.people === '2인'}
          />
          <GeneralButton
            title={'3인'}
            onClick={() => {
              this.props.setPeople('3인');
            }}
            clicked={this.props.people === '3인'}
          />
          <GeneralButton
            title={'4인'}
            onClick={() => {
              this.props.setPeople('4인');
            }}
            clicked={this.props.people === '4인'}
          />
        </View>
      </View>
    );
  }
}
