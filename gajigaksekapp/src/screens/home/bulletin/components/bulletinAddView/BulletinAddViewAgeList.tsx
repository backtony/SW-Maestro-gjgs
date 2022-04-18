import {englishAge} from '@/utils/commonParam';
import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import GeneralButton from '../../../../../components/GeneralButton';

interface BulletinAddViewAgeListProps {
  age: string;
  setAge: (age: string) => void;
}

export default class BulletinAddViewAgeList extends React.Component<
  BulletinAddViewAgeListProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>연령</Text>
        <ScrollView horizontal={true}>
          {Object.keys(englishAge).map((key: string) => (
            <GeneralButton
              title={englishAge[key]}
              onClick={() => this.props.setAge(key)}
              clicked={this.props.age === key}
            />
          ))}
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
