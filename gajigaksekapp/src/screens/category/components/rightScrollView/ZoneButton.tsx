import {CategoryPair} from '@/utils/Category';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity} from 'react-native';

interface ZoneButtonProps {
  navigation: any;
  subCategory: CategoryPair;
}

export default class ZoneButton extends React.Component<ZoneButtonProps, {}> {
  private keyCount: number = 0;
  render() {
    return (
      <TouchableOpacity
        key={this.keyCount++}
        style={this.zoneButtonStyle.button}
        onPress={() => {
          this.props.navigation.navigate('lectureSearchResult', {
            keyword: null,
            selectedSubZone: this.props.subCategory.sub,
            categoryIdList: [],
          });
        }}>
        <Text style={this.zoneButtonStyle.text}>
          {this.props.subCategory.sub}
        </Text>
      </TouchableOpacity>
    );
  }

  private zoneButtonStyle = StyleSheet.create({
    button: {
      width: '45%',
      height: 44,
      backgroundColor: '#f5f7ff',
      borderRadius: 8,
      marginHorizontal: 5,
      marginBottom: 10,
      justifyContent: 'center',
      paddingLeft: 10,
    },
    text: {fontSize: 12, color: '#5564b0'},
  });
}
