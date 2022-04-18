import Category from '@/utils/Category';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Image, Text, View} from 'react-native';

interface CategoryTitleProps {
  mainCategory: string;
}

export default class CategoryTitle extends React.Component<
  CategoryTitleProps,
  {}
> {
  render() {
    return (
      <View style={this.categoryTitleStyle.button}>
        <Image
          style={this.categoryTitleStyle.image}
          source={Category.getMainCategoryImageUrl(this.props.mainCategory)}
        />
        <Text style={this.categoryTitleStyle.text}>
          {this.props.mainCategory}
        </Text>
      </View>
    );
  }

  private categoryTitleStyle = StyleSheet.create({
    button: {
      flexDirection: 'row',
      alignItems: 'center',
      width: '100%',
      marginLeft: 10,
      marginTop: 10,
      marginBottom: 10,
    },
    image: {width: 16, height: 16},
    text: {
      fontSize: 14,
      fontWeight: 'bold',
      color: '#1d1d1f',
      marginTop: 6,
      marginLeft: 4,
    },
  });
}
