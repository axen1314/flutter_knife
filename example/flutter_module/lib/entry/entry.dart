import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Entry extends StatelessWidget {
  final String name;

  Entry(this.name);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: Scaffold(
        body: Center(
          child: Text(name),
        ),
      ),
    );
  }
}
