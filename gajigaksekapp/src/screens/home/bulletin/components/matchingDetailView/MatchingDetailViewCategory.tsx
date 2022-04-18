import Category from '@/utils/Category';
import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface MatchingDetailViewCategoryProps {
  mainCategory: string;
  selectedMainCategory: string;
  selectedSubCategory: string;
  setSubCategory: (sub: string) => void;
}

export default class MatchingDetailViewCategory extends React.Component<
  MatchingDetailViewCategoryProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={{flexDirection: 'row'}}>
          <Image
            style={{width: 16, height: 16}}
            source={Category.getMainCategoryImageUrl(this.props.mainCategory)}
          />
          <Text style={this.styles.text}>
            {this.props.selectedMainCategory}
          </Text>
        </View>

        <View>
          <View style={this.styles.wrapper}>
            {Category.getSubCategoryList(this.props.mainCategory).map(sub => (
              <TouchableOpacity
                key={sub.id}
                style={[
                  this.styles.button,
                  this.props.selectedSubCategory === sub.sub
                    ? {backgroundColor: '#4f6cff'}
                    : {},
                ]}
                onPress={() => {
                  this.props.setSubCategory(sub.sub);
                }}>
                <Text
                  style={[
                    {fontSize: 12, color: '#5564b0'},
                    this.props.selectedSubCategory === sub.sub
                      ? {color: '#FFF'}
                      : {},
                  ]}>
                  {sub.sub}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginLeft: 5,
    },
    wrapper: {
      flexDirection: 'row',
      marginLeft: 5,
      marginVertical: 5,
      flexWrap: 'wrap',
    },
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
  });
}
