import Category from '@/utils/Category';
import React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

interface LectureSearchViewPopularKeywordProps {
  setKeyword: (keyword: string) => void;
}

export default class LectureSearchViewPopularKeyword extends React.Component<
  LectureSearchViewPopularKeywordProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>인기 검색어</Text>
        <ScrollView horizontal={true}>
          {Category.getSubCategoryList('액티비티').map(item => (
            <TouchableOpacity
              style={this.styles.button}
              onPress={() => {
                this.props.setKeyword(item.sub);
              }}>
              <Text style={this.styles.text2}>{item.sub}</Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
      color: '#1d1d1f',
    },
    button: {
      backgroundColor: '#f5f7ff',
      padding: 10,
      paddingBottom: 7,
      borderRadius: 6,
      flexDirection: 'row',
      marginRight: 10,
      alignItems: 'center',
      justifyContent: 'center',
      borderColor: '#4f6cff',
      borderWidth: 1,
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4f6cff',
    },
  });
}
