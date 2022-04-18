import {CategoryPair} from '@/utils/Category';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity} from 'react-native';

interface CategoryButtonProps {
  navigation: any;
  subCategory: CategoryPair;
}

export default class RightScrollViewCategoryButton extends React.Component<
  CategoryButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.categoryButtonStyle.button}
        onPress={() => {
          this.props.navigation.navigate('lectureSearchResult', {
            keyword: '',
            selectedSubZone: '',
            categoryIdList: [this.props.subCategory.id],
          });
        }}>
        <Text style={this.categoryButtonStyle.text}>
          {this.props.subCategory.sub}
        </Text>
      </TouchableOpacity>
    );
  }

  private categoryButtonStyle = StyleSheet.create({
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
