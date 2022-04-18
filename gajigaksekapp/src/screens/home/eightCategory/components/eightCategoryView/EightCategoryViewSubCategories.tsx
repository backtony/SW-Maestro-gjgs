import Category from '@/utils/Category';
import React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

interface EigthCategoryViewSubCategoriesProps {
  mainCategory: string;
  categoryIdList: number[];
  onPressAll: () => void;
  setSubCategory: (id: number) => void;
}

export default class EigthCategoryViewSubCategories extends React.Component<
  EigthCategoryViewSubCategoriesProps,
  {}
> {
  render() {
    return (
      <View>
        <ScrollView
          horizontal={true}
          style={{marginLeft: 28}}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <TouchableOpacity
            style={[this.styles.button]}
            onPress={() => {
              this.props.onPressAll();
            }}>
            <Text style={[this.styles.text]}>전체</Text>
          </TouchableOpacity>
          {Category.getSubCategoryList(this.props.mainCategory).map(sub => (
            <TouchableOpacity
              key={sub.id}
              style={[
                this.styles.button2,
                this.props.categoryIdList.length === 1 &&
                this.props.categoryIdList[0] === +sub.id
                  ? {backgroundColor: '#4f6cff'}
                  : {},
              ]}
              onPress={() => {
                this.props.setSubCategory(+sub.id);
              }}>
              <Text
                style={[
                  this.styles.text2,
                  this.props.categoryIdList.length === 1 &&
                  this.props.categoryIdList[0] === +sub.id
                    ? {color: '#FFF'}
                    : {},
                ]}>
                {sub.sub}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    button: {
      backgroundColor: '#f5f5f7',
      padding: 10,
      borderRadius: 6,
      flexDirection: 'row',
      marginRight: 10,
      alignItems: 'center',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4a4a4c',
    },
    button2: {
      backgroundColor: '#f5f5f7',
      padding: 10,
      borderRadius: 6,
      flexDirection: 'row',
      marginRight: 10,
      alignItems: 'center',
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4a4a4c',
    },
  });
}
