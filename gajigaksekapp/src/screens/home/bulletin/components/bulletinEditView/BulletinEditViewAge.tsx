import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';

interface BulletinEditViewAgeProps {
  age: string;
  setAge: (age: string) => void;
}

export default class BulletinEditViewAge extends React.Component<
  BulletinEditViewAgeProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>연령</Text>
        <ScrollView horizontal={true}>
          <GeneralButton
            title={'20~25세'}
            onClick={() => {
              this.props.setAge('TWENTY_TO_TWENTYFIVE');
            }}
            clicked={this.props.age === 'TWENTY_TO_TWENTYFIVE'}
          />
          <GeneralButton
            title={'25~30세'}
            onClick={() => {
              this.props.setAge('TWENTYFIVE_TO_THIRTY');
            }}
            clicked={this.props.age === 'TWENTYFIVE_TO_THIRTY'}
          />
          <GeneralButton
            title={'30~35세'}
            onClick={() => {
              this.props.setAge('THIRTY_TO_THIRTYFIVE');
            }}
            clicked={this.props.age === 'THIRTY_TO_THIRTYFIVE'}
          />
          <GeneralButton
            title={'35~40세'}
            onClick={() => {
              this.props.setAge('THIRTYFIVE_TO_FORTY');
            }}
            clicked={this.props.age === 'THIRTYFIVE_TO_FORTY'}
          />
          <GeneralButton
            title={'40세 이상'}
            onClick={() => {
              this.props.setAge('FORTY');
            }}
            clicked={this.props.age === 'FORTY'}
          />
        </ScrollView>
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
